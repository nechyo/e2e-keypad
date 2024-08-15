import '../style/keypad.css';

export default function SecureKeypad({ keypad, onKeyPressed }) {
    const rows = 3
    const cols = 4
    
    return (
        <table className='table-style'>
            <tbody>
                {Array.from({ length: rows }).map((_, rowIndex) => (
                    <tr key={rowIndex}>
                        {Array.from({ length: cols }).map((_, colIndex) => {
                            const keyIndex = rowIndex * cols + colIndex;
                            const key = keypad["keys"][keyIndex];
                            const row = Math.floor(keyIndex / cols);
                            const col = keyIndex % cols;
                            const backgroundPositionX = (col / (cols - 1)) * 100;
                            const backgroundPositionY = (row / (rows - 1)) * 100;
            
                            return (
                                <td className="td-style" key={keyIndex}>
                                    <button 
                                        className="button-style"
                                        onClick={() => {
                                            if (key === "") return
                                            onKeyPressed(rowIndex, colIndex)
                                        }}
                                        style={{
                                            fontSize: '10px',
                                            backgroundImage: `url(${keypad["image"]})`,
                                            backgroundPosition: `${backgroundPositionX}% ${backgroundPositionY}%`,
                                            backgroundSize: `${100 * cols}% ${100 * rows}%`
                                        }}
                                    />
                                </td>
                            );
                        })}
                    </tr>
                ))}
            </tbody>
        </table>
    );
}
