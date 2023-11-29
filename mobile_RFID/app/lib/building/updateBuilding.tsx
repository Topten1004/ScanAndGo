import axios from "axios"
import handleError from "../handleError"
import { BACKEND_URL } from "../consts"
import { IBuilding } from "../../types/build"

const updateBuilding = async (data: IBuilding) => {
  try {
    const response = await axios.put(`${BACKEND_URL}/building/update?id=${data.id}`, {
      name: data.name,
    })
    return response.data
  } catch (error) {
    handleError(error)
    return { error }
  }
}

export default updateBuilding
