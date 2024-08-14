package bob.e2e.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("hash")
data class E2eKeypadHash(
    @Id val id: String,
    val hashes: List<String>,
    val nums: List<String>
)