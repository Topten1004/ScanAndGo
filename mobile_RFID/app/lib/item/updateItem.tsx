import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"
import { IItem } from "../../types/item"

const updateItem = async (data: IItem) => {
  try {
    const response = await axios.put(`${BACKEND_URL}/item/update?id=${data.id}`, {
      name: data.name,
      category_id: data.category_id,
      barcode: data.barcode
    })
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default updateItem
