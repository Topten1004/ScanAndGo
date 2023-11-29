import React from "react"
import { View, StyleSheet, ScrollView, Dimensions } from "react-native"
import FooterLayout from "./footer"

const ContainerLayout = ({ children, navigation }: any) => {
  return (
    <View style={styles.container}>
      {/* <HeaderLayout navigation={navigation} /> */}
      <ScrollView style={styles.board}>{children}</ScrollView>
      <FooterLayout navigation={navigation} />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    width: "100%",
    height: "100%",
  },
  board: {
    maxHeight: Dimensions.get("window").height - 125,
  },
})

export default ContainerLayout
