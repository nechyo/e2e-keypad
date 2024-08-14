package bob.e2e.data.repository

import bob.e2e.domain.model.E2eKeypadHash
import org.springframework.data.repository.CrudRepository

interface E2eKeypadRedisRepository : CrudRepository<E2eKeypadHash, String>