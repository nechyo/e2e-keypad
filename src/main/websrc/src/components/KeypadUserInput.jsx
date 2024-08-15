import '../style/keypad.css'

export default function KeypadUserInput({ userInput }) {
    return (
        <div className="input-group-style">
            <div className="number-style"/>
            <div>
                {Array.from({ length: 6 }).map((_, index) => (
                    <span 
                        key={index} 
                        className={`input-char-style ${userInput && userInput[index] ? 'active' : ''}`}
                    ></span>
                ))}
            </div>
        </div>
    );
}
