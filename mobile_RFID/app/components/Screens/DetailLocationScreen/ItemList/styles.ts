import { StyleSheet } from "react-native"

const styles = StyleSheet.create({
  container: {
    width: "100%",
    marginTop: 10,
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 10,
  },
  item: {
    backgroundColor: "blue",
    justifyContent: "center",
    alignItems: "center",
    width: 110,
    minHeight: 70,
    paddingHorizontal: 10,
    paddingVertical: 10,
    borderRadius: 10,
  },
  itemName: {
    color: "white",
    textAlign: "center",
    minWidth: 100,
    minHeight: 40
  },
})

export default styles
