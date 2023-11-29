import React from "react"
import { DimensionValue, TextInput } from "react-native"

interface IInput {
  bgColor: string
  placeholder: string
  value: string
  setValue: React.Dispatch<React.SetStateAction<string>>
  secureTextEntry: boolean
  width: DimensionValue | undefined
}

const _Input = ({ bgColor, placeholder, value, setValue, secureTextEntry, width }: IInput) => {
  return (
    <TextInput
      style={{
        borderWidth: 1,
        borderColor: "#999",
        backgroundColor: bgColor,
        paddingLeft: 15,
        borderRadius: 10,
        width: width,
      }}
      placeholderTextColor={"#999"}
      placeholder={placeholder}
      value={value}
      onChange={(e) => setValue(e.nativeEvent.text)}
      secureTextEntry={secureTextEntry}
    />
  )
}

_Input.defaultProps = {
  bgColor: "#f7f7f7",
  placeholder: "",
  secureTextEntry: false,
  width: "100%",
}

export default _Input
