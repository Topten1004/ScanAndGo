import Layout from "../../Layout"
import ItemList from "./ItemList"
import InputItem from "./InputItem"

const ItemScreen = () => {
  return (
    <Layout type="base">
      <InputItem />
      <ItemList />
    </Layout>
  )
}

export default ItemScreen
