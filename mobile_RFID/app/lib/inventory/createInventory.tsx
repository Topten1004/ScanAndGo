import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"

const createInventory = async (
  status: string,
  barcode: string,
  photo: string,
  comment: string,
  category_id: number,
  item_id: number,
  building_id: number,
  area_id: number,
  floor_id: number,
  detail_location_id: number
) => {
  try {
    const response = await axios.put(`${BACKEND_URL}/inventory/create`, {
      barcode,
      status,
      photo,
      comment,
      category_id,
      item_id,
      building_id,
      area_id,
      floor_id,
      detail_location_id
    })
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default createInventory
