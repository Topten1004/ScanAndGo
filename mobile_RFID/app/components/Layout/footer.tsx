import * as React from "react"
import { View, StyleSheet, TouchableOpacity } from "react-native"
import _Text from "../../shared/core/ui/_Text"
// import { Shadow } from 'react-native-shadow-2';

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

const FooterLayout = ({ navigation }: any) => {
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
    // <Shadow distance={80} offset={[0, 0]}>
    <View style={styles.container}>
      {list.map((item, index) => (
        <TouchableOpacity key={index} onPress={item.onPress}>
          <_Text text={`${item.label}`} />
        </TouchableOpacity>
      ))}
    </View>
    // </Shadow>
  )
}

export default FooterLayout
