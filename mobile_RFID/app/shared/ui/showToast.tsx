import { ToastAndroid, Platform, Alert } from "react-native"

const showToast = (message: any) => {
  if (Platform.OS == "android") {
    ToastAndroid.showWithGravity(message, ToastAndroid.SHORT, ToastAndroid.BOTTOM)
  } else if (Platform.OS == "ios") {
    Alert.alert(message)
  }
}

export default showToast
