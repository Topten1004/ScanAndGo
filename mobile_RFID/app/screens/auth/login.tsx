import React, { useState } from "react"
import { View, StyleSheet, Image } from "react-native"
import _Input from "../../shared/core/ui/_Input"
import _Button from "../../shared/core/ui/_Button"
import _Text from "../../shared/core/ui/_Text"
import http from "../../service/http-common"
import { showToastWithGravity } from "../../shared/helper/toast"
import { useAppDispatch } from "../../store/hooks"
import { setAccount, setToken } from "../../store/slices/auth.slice"

const logo = require("../../assets/logo.png")

const LogIn = ({ navigation }: any) => {
  const dispath = useAppDispatch()

  const [username, setUsername] = useState("admin")
  const [password, setPassword] = useState("admin")

  const [status, setStatus] = useState("")

  const handleLogIn = async () => {
    try {
      console.log({ username, password })
      const res = await http.post("/user/signin", { username, password })
      console.log(res?.data)
      setStatus(res?.data)
      if (res) {
        if (res.data.status == -1) {
          showToastWithGravity("User Not Found")
        } else if (res.data.status == 0) {
          showToastWithGravity("Password Not correct")
        } else if (res.data.user.role == 0) {
          showToastWithGravity("Your account is pending now")
        } else if (res.data.status == 1) {
          showToastWithGravity("Sign In Successfully")
          dispath(setAccount(res.data.user))
          dispath(setToken(res.data.access_token))
          navigation.navigate("Category", { from: "LogIn" })
        }
      }
    } catch (error) {
      console.log(error)
    }
  }

  return (
    <View style={styles.container}>
      <View style={styles.logo}>
        <Image source={logo} />
      </View>
      <View style={styles.form}>
        <View style={styles.input}>
          <_Text text="Username" />
          <_Input placeholder="username" value={username} setValue={setUsername} />
        </View>
        <View style={styles.input}>
          <_Text text="Password" />
          <_Input
            placeholder="password"
            value={password}
            setValue={setPassword}
            secureTextEntry={true}
          />
        </View>
        <_Button text="LogIn" py={15} onPress={handleLogIn} filled />
      </View>
      <_Text text={String(status)} />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: "white",
    width: "100%",
    height: "100%",
  },
  logo: {
    alignItems: "center",
  },
  form: {
    gap: 20,
    marginTop: 60,
    paddingHorizontal: "5%",
  },
  input: {
    gap: 10,
  },
})

export default LogIn
