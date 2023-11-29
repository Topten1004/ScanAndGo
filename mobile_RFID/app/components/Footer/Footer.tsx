import * as React from "react"
import { View, StyleSheet, TouchableOpacity } from "react-native"
import _Text from "../../shared/core/ui/_Text"
import { useNavigation } from "@react-navigation/native"

const styles = StyleSheet.create({
  container: {
    position: "absolute",
    bottom: 0,
    left: 0,
    width: "100%",
    backgroundColor: "white",

    flexDirection: "row",
    justifyContent: "space-around",

    paddingTop: 15,
    paddingBottom: 30,
  },
})

const Footer = () => {
  const navigation: any = useNavigation()

  const list = [
    {
      label: "Category",
      onPress: () => {
        navigation.navigate("Category")
      },
    },
    {
      label: "Building",
      onPress: () => {
        navigation.navigate("Building")
      },
    },
    {
      label: "Inventory",
      onPress: () => {
        navigation.navigate("Inventory")
      },
    },
  ]

  return (
    <View style={styles.container}>
      {list.map((item, index) => (
        <TouchableOpacity key={index} onPress={item.onPress}>
          <_Text text={`${item.label}`} />
        </TouchableOpacity>
      ))}
    </View>
  )
}

export default Footer
