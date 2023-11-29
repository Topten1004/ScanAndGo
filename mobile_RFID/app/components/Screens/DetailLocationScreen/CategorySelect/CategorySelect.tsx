import { View } from "react-native"
import SelectDropdown from "react-native-select-dropdown"
import { useLCI } from "../../../../providers/LCIProvider"
import styles from "./styles"

const CategorySelect = () => {
  const { categories, setSelectedCategory, selectedCategory } = useLCI()

  return (
    <View style={styles.selectView}>
      <SelectDropdown
        data={categories}
        onSelect={(selectedItem) => {
          setSelectedCategory(selectedItem)
          return selectedItem.name
        }}
        buttonTextAfterSelection={(selectedItem) => selectedItem.name}
        rowTextForSelection={(item) => item.name}
        defaultButtonText={selectedCategory?.name || "Select a category."}
      />
    </View>
  )
}

export default CategorySelect
