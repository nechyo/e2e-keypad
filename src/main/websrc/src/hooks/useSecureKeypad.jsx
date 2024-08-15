"use client";

import {useMemo, useState} from 'react';
import axios from "axios";
import {JSEncrypt} from "jsencrypt";

export default function useSecureKeypad() {
  const [keypad, setKeypad] = useState(null);
  const [userInput, setUserInput] = useState(null);

  const getSecureKeypad = () => {
    axios.get('/api/e2e-keypad').then(e=> setKeypad(e.data))
  }

  const onKeyPressed = (row, col) => {
    setUserInput((prevUserInput) => {
      if (prevUserInput == null) return [keypad["keys"][row * 4 + col]]
      else return [...prevUserInput, keypad["keys"][row * 4 + col]]
    })
  }

  const sendUserInput = () => {
    if (userInput.length === 6) {
      alert(userInput.join('\n'))
      setUserInput(null)
      getSecureKeypad()
    }
  }

  return {
    states: {
      keypad,
      userInput,
    },
    actions: {
      getSecureKeypad,
      onKeyPressed,
      sendUserInput
    }
  }
}
