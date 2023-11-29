import { StyleSheet } from "react-native"

const styles = StyleSheet.create({
  buildItem: {
    backgroundColor: "blue",
    justifyContent: "center",
    alignItems: "center",
    width: 120,
    paddingHorizontal: 10,
    paddingVertical: 10,
    borderRadius: 10,
  },
  buildName: {
    color: "white",
    textAlign: "center",
    minWidth: 100,
    minHeight: 40
  },
  icons: {
    marginTop: 5,
    flexDirection: "row",
    gap: 5,
    width: "100%",
    justifyContent: "flex-end",
  },
})

export default styles
