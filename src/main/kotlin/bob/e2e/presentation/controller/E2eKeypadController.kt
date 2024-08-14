package bob.e2e.presentation.controller

import bob.e2e.domain.model.E2eKeypad
import bob.e2e.domain.model.E2eKeypadHash
import bob.e2e.domain.service.E2eKeypadService
import bob.e2e.presentation.dto.E2eKeypadResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/e2e-keypad")
@RestController
class E2eKeypadController(
    private val E2eKeypadService: E2eKeypadService,
) {

    @GetMapping("/")
    @Suppress("UNCHECKED_CAST") // <- 하면 안되는 짓
    fun getKeypad(userId: String): E2eKeypadResponseDto {
        val keypad = E2eKeypadService.getE2eKeypad(userId)
        return  E2eKeypadResponseDto(keys= keypad["hashes"] as List<String>, image= keypad["image"] as String)
    }
}
