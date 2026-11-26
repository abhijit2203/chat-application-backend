# Live messaging (WebSocket + STOMP)

This implementation keeps the existing REST APIs and uses the existing `MessageService`, `SendMessageRequestDTO`, and `MessageResponseDTO` for WebSocket messages.

## Destinations

- WebSocket handshake: `ws://localhost:2026/ws`
- STOMP CONNECT native header: `Authorization: Bearer <JWT>`
- Send destination: `/app/conversations/{conversationId}/messages`
- Subscribe destination: `/topic/conversations/{conversationId}`
- Message JSON: `{"content":"Hello"}`

## How it works

1. The STOMP CONNECT frame is authenticated by `JwtChannelInterceptor` using the same `JwtService` as REST authentication.
2. Subscription destinations are restricted to `/topic/conversations/{id}` and membership is checked against `ConversationMemberRepository`.
3. Send destinations are restricted to `/app/conversations/{id}/messages`.
4. `WebSocketMessageController` resolves the sender from the authenticated STOMP principal and calls the existing `MessageService.sendMessage(...)`.
5. `MessageService` verifies conversation membership and saves the message. The returned `MessageResponseDTO` is broadcast to the conversation topic.

## Browser test client

1. Start MySQL and ensure `src/main/resources/application.properties` has correct credentials.
2. Start the backend from this project directory using `mvnw.cmd spring-boot:run` on Windows or `./mvnw spring-boot:run` on macOS/Linux (make the wrapper executable if needed).
3. Open `tools/websocket-test.html` using VS Code Live Server on `http://localhost:5500`. The configured WebSocket allowed origins include this origin.
4. Use the existing login REST API to obtain a JWT for a user. Paste the token value into the page; `Bearer ` prefix is optional.
5. Enter a real conversation ID that the user belongs to, click **Connect**, then **Subscribe**.
6. Open the same page in a second tab and repeat with a second member's JWT and the same conversation ID.
7. Send a message from one tab. The other subscribed tab should receive the saved message without refreshing.

## Troubleshooting

- `Input error`: the page needs a JWT and valid WebSocket URL. Conversation ID is needed for Subscribe/Send, not Connect.
- WebSocket closes at connect: inspect the Spring Boot console. Common causes are invalid/expired JWT, wrong URL/port, backend not running, or the page being served from an origin not allowed by `WebSocketConfig`.
- Subscribe denied: the logged-in user must be a member of that conversation.
- Send fails: check the server logs; `MessageService` rejects non-members, missing conversations, blank content, and invalid payloads.
- Browser must be online to the backend. This uses the in-memory simple broker, so it is suitable for this single-instance app; a production multi-instance deployment would normally use an external broker.

## Scope

No separate WebSocket DTO is required: `SendMessageRequestDTO` is used for incoming messages and `MessageResponseDTO` for broadcasts. Existing REST message history/send endpoints remain available.
