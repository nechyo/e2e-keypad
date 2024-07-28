package bob.e2e.presentation.dto

import bob.e2e.domain.model.E2eKeypad

data class E2eKeypadResponseDto (
    val id: String,
    val hash: String,
    val image: String
) {
    companion object {
        fun from(e2eKeypad: E2eKeypad) =
            E2eKeypadResponseDto(
                id = e2eKeypad.id,
                hash = e2eKeypad.hash,
                image = e2eKeypad.image
            )
    }
}