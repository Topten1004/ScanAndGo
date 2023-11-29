import React from "react"
import { View, StyleSheet, ScrollView, Dimensions } from "react-native"
import Footer from "../../Footer"

const BaseLayout = ({ children }: any) => {
  return (
    <View style={styles.container}>
      <ScrollView style={styles.board}>{children}</ScrollView>
      <Footer />
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
    padding: 20,
  },
})

export default BaseLayout
