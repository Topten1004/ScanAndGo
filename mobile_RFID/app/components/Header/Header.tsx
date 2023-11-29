import React from "react"
import { StyleSheet, TouchableOpacity, View } from "react-native"
import _Text from "../../shared/core/ui/_Text"
import { useAppDispatch } from "../../store/hooks"
import { setAccount, setToken } from "../../store/slices/auth.slice"

const Header = ({ navigation }: any) => {
  const dispatch = useAppDispatch()

  const handleLogout = () => {
    navigation.navigate("LogIn")
    dispatch(setToken(""))
    dispatch(setAccount({}))
  }

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={handleLogout}>
        <_Text text="Logout" size={20} lineHeight={20} />
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: "2%",
    marginTop: "2%",
  },
})

export default Header
