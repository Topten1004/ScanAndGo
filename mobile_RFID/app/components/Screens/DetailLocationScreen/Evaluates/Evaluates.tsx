import { TouchableOpacity, Image, View } from "react-native"
import Icons from "../../../../shared/core/Icon"
import styles from "./styles"
import { useDetailLocation } from "../../../../providers/DetailLocationProvider"
import { useLCI } from "../../../../providers/LCIProvider"

const Evaluates = () => {
  const { HappyIcon, IdleIcon, CryIcon } = Icons
  const { evaluate } = useDetailLocation()
  const { selectedItem, selectedCategory } = useLCI()

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={() => evaluate(1, selectedItem, selectedCategory)}>
        <Image source={CryIcon} />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => evaluate(2, selectedItem, selectedCategory)}>
        <Image source={IdleIcon} />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => evaluate(3, selectedItem, selectedCategory)}>
        <Image source={HappyIcon} />
      </TouchableOpacity>
    </View>
  )
}

export default Evaluates
