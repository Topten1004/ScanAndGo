import { useDetailLocation } from "../../../../providers/DetailLocationProvider"
import Input from "../../../../shared/core/Input"
import { View } from "react-native"
import styles from "./styles"

const BarCodeInput = () => {
  const { barCode, setBarCode } = useDetailLocation()

  return (
    <View style={styles.inputView}>
      <Input placeholder="Enter a bar code." value={barCode} setValue={setBarCode} width={200} />
    </View>
  )
}

export default BarCodeInput
