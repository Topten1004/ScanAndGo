import * as React from "react"
import { Text } from "react-native"

interface IText {
  text: string
  size: number
  weight:
    | "normal"
    | "bold"
    | "100"
    | "200"
    | "300"
    | "400"
    | "500"
    | "600"
    | "700"
    | "800"
    | "900"
    | undefined
  color: string
  align: "auto" | "left" | "right" | "center" | "justify" | undefined
  lineHeight: number | undefined
  textTransform: "none" | "capitalize" | "uppercase" | "lowercase" | undefined
}

const _Text = ({ text, size, weight, color, align, lineHeight, textTransform }: IText) => {
  return (
    <Text
      style={{
        fontSize: size,
        fontWeight: weight,
        color: color,
        textAlign: align,
        lineHeight: lineHeight,
        textTransform: textTransform,
      }}
    >
      {text}
    </Text>
  )
}

_Text.defaultProps = {
  weight: "normal",
  color: "black",
  align: "auto",
  lineHeight: 15,
  size: 14,
  textTransform: "none",
}

export default _Text
