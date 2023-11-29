import { View } from "react-native"
import styles from "./styles"
import { useCategory } from "../../../../providers/CategoryProvider"
import { ICategory } from "../../../../types/category"
import CategoryItem from "../CategoryItem"

const CategoryList = () => {
  const { categories } = useCategory()

  return (
    <View style={styles.container}>
      {categories.map((category: ICategory) => (
        <CategoryItem key={category.id} data={category} />
      ))}
    </View>
  )
}

export default CategoryList
