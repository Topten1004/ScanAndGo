import { View } from "react-native"
import styles from "./styles"
import Input from "../../../../shared/core/Input"
import AddItemButton from "../AddItemButton"
import UpdateItemButton from "../UpdateItemButton"
import { useItem } from "../../../../providers/ItemProvider"

const InputItem = () => {
  const { itemName, setItemName, isEditMode } = useItem()

  return (
    <View style={styles.container}>
      <Input
        placeholder="Enter a item name."
        value={itemName}
        setValue={setItemName}
        width={200}
      />
      {isEditMode ? <UpdateItemButton /> : <AddItemButton />}
    </View>
  )
}

export default InputItem
