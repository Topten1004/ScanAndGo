import { View } from "react-native"
import styles from "./styles"
import { IItem } from "../../../../types/item"
import { useItem } from "../../../../providers/ItemProvider"
import Item from "../Item"

const ItemList = () => {
  const { items } = useItem()

  return (
    <View style={styles.container}>
      {items.map((item: IItem) => (
        <Item key={item.id} data={item} />
      ))}
    </View>
  )
}

export default ItemList
