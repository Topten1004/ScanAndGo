import Layout from "../../Layout"
import CategoryList from "./CategoryList"
import InputCategory from "./InputCategory"

const CategoryScreen = () => {
  return (
    <Layout type="base">
      <InputCategory />
      <CategoryList />
    </Layout>
  )
}

export default CategoryScreen
