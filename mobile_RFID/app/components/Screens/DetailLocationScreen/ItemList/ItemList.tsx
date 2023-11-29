import { View, TouchableOpacity, Text } from "react-native"
import { useLCI } from "../../../../providers/LCIProvider"
import styles from "./styles"
import { STEPS } from "../../../../consts/detailStep"
import { useDetailLocation } from "../../../../providers/DetailLocationProvider"
import { IItem } from "../../../../types/item"

const ItemList = () => {
  const { items, setSelectedItem } = useLCI()
  const { setCurrentStep } = useDetailLocation()

  return (
    <View style={styles.container}>
      {items.map((item: IItem) => (
        <TouchableOpacity
          key={item.id}
          onPress={() => {
            setSelectedItem(item)
            setCurrentStep(STEPS.INPUT_BAR_CODE)
          }}
        >
          <View style={styles.item}>
            <Text style={styles.itemName}>{item.name}</Text>
          </View>
        </TouchableOpacity>
      ))}
    </View>
  )
}

export default ItemList
