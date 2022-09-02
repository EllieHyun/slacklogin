package com.example.slacklogin

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.request.oauth.OAuthV2AccessRequest
import com.slack.api.methods.request.team.TeamInfoRequest
import com.slack.api.methods.request.users.UsersInfoRequest
import com.slack.api.methods.request.users.profile.UsersProfileGetRequest
import com.slack.api.model.block.Blocks
import com.slack.api.model.block.composition.BlockCompositions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AutoService @Autowired constructor(
    @Value("\${slack.client-id}") var clientId: String,
    @Value("\${slack.client-secret}") var clientSecret: String,
    @Value("\${slack.redirect-uri}") var redirectUri: String
) {

    fun login(code: String) {

        val slackClient: MethodsClient = Slack.getInstance().methods()

        // 1.token 발급 요청
        val request = OAuthV2AccessRequest.builder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .redirectUri(redirectUri)
            .code(code)
            .build()

        val oauthV2Access = slackClient.oauthV2Access(request)

        println("request = $oauthV2Access")

        val userId = oauthV2Access.authedUser.id
        val accessToken = oauthV2Access.authedUser.accessToken

        // 2. user info 조회
        val usersInfoRequest = UsersInfoRequest.builder()
            .token(accessToken)
            .user(userId)
            .build()

        val usersInfo = slackClient.usersInfo(usersInfoRequest)

        println("user = ${usersInfo.user}")

        // 3. team info 조회
        val teamInfoRequest = TeamInfoRequest.builder()
            .token(accessToken)
            .build()

        val teamInfo = slackClient.teamInfo(teamInfoRequest)
        println("team = ${teamInfo.team}")

        // 4. user profile 조회
        val profileGetRequest = UsersProfileGetRequest.builder().token(accessToken).build()
        val usersProfile = slackClient.usersProfileGet(profileGetRequest)
        println("user profile = $usersProfile")

        // slack message 보내기
        val result = slackClient.chatPostMessage { r: ChatPostMessageRequest.ChatPostMessageRequestBuilder ->
            r
                .token(accessToken)
                .channel(userId)
                .blocks(Blocks.asBlocks(
                    Blocks.section { section -> section.text(BlockCompositions.markdownText(":yum: Hello World!! ${usersInfo.user.name}")) }
                ))
        }

        println("result = ${result.message}")
    }
}