import * as React from "react"
import { View, Text, TouchableOpacity } from "react-native"

interface IButton {
  text: string
  bgColor: string
  color: string
  borderRadius: number
  width?: string | number
  px: number | "auto"
  py: number
  filled: boolean
  onPress: () => void
}

const Button = ({ text, bgColor, color, borderRadius, px, py, filled, onPress }: IButton) => {
  return (
    <TouchableOpacity onPress={onPress}>
      <View
        style={{
          justifyContent: "center",
          alignItems: "center",
          backgroundColor: filled == true ? bgColor : "rgba(0,0,0,0)",
          borderWidth: 1,
          borderColor: "#1b67ff",
          borderRadius: borderRadius,
          paddingVertical: py,
          paddingHorizontal: px,
        }}
      >
        <Text style={{ color: color }}>{text}</Text>
      </View>
    </TouchableOpacity>
  )
}

Button.defaultProps = {
  filled: false,
  borderRadius: 3,
  color: "white",
  bgColor: "#1b67ff",
  px: "auto",
}

export default Button
