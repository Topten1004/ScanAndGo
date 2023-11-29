import { View } from "react-native"
import styles from "./styles"
import Input from "../../../../shared/core/Input"
import AddFloorButton from "../AddFloorButton"
import UpdateCategoryButton from "../UpdateCategoryButton"
import { useCategory } from "../../../../providers/CategoryProvider"

const InputCategory = () => {
  const { categoryName, setCategoryName, isEditMode } = useCategory()

  return (
    <View style={styles.container}>
      <Input
        placeholder="Enter a category name."
        value={categoryName}
        setValue={setCategoryName}
        width={200}
      />
      {isEditMode ? <UpdateCategoryButton /> : <AddFloorButton />}
    </View>
  )
}

export default InputCategory
