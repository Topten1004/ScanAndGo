import { Text, TouchableOpacity, View } from "react-native"
import { FC } from "react"
import styles from "./styles"
import AntDesign from "react-native-vector-icons/AntDesign"
import Entypo from "react-native-vector-icons/Entypo"
import { useNavigation } from "@react-navigation/native"
import { ICategory } from "../../../../types/category"
import { useCategory } from "../../../../providers/CategoryProvider"

interface CategoryItemProps {
  data: ICategory
}

const CategoryItem: FC<CategoryItemProps> = ({ data }) => {
  const { openEditMode, deleteCategoryById } = useCategory()
  const navigation: any = useNavigation()

  return (
    <View style={styles.categoryItem}>
      <TouchableOpacity
        onPress={() =>
          navigation.navigate("Item", {
            categoryId: data.id,
          })
        }
      >
        <Text style={styles.categoryName}>{data.name}</Text>
      </TouchableOpacity>
      <View style={styles.icons}>
        <TouchableOpacity onPress={() => openEditMode(data)}>
          <Entypo name="edit" color="white" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => deleteCategoryById(data.id)}>
          <AntDesign name="delete" color="white" />
        </TouchableOpacity>
      </View>
    </View>
  )
}

export default CategoryItem
