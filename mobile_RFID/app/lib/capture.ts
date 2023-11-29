import { launchCamera, CameraOptions } from "react-native-image-picker"
import showToast from "../shared/ui/showToast"
import convertImageToBase64 from "./convertImageToBase64"

const capture = async (handleCapture: any) => {
  const options: CameraOptions = { mediaType: "photo" }

  await launchCamera(options, async (response) => {
    if (response.didCancel) {
      showToast("User cancelled image picker")
    } else {
      if (response.assets?.length) {
        convertImageToBase64(response.assets[0], handleCapture)
      }
    }
  })
}

export default capture
