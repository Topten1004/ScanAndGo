import { View, Image } from "react-native"
import { useDetailLocation } from "../../../../providers/DetailLocationProvider"
import styles from "./styles"

const CapturedImage = () => {
  const { capturedImg } = useDetailLocation()

  return (
    <View style={styles.cameraBoard}>
      <Image
        source={{ uri: capturedImg }}
        style={styles.importedphoto}
        width={
          capturedImg?.width == undefined ? 300 : (capturedImg?.width * 300) / capturedImg?.height
        }
      />
    </View>
  )
}

export default CapturedImage
