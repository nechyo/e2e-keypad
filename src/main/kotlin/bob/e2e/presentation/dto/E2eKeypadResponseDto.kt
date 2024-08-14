package bob.e2e.presentation.dto

import bob.e2e.domain.model.E2eKeypad

data class E2eKeypadResponseDto(
    val keys: List<String>,
    val image: String
) {
    companion object {
        fun from(keys: List<String>, image: String) =
            E2eKeypadResponseDto(
                keys = keys,
                image = image
            )
    }
}