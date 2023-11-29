import { createContext, useContext, useMemo, useState, useEffect, useCallback } from "react"
import createFloor from "../lib/floor/createFloor"
import { IFloor } from "../types/floor"
import deleteFloor from "../lib/floor/deleteFloor"
import updateFloor from "../lib/floor/updateFloor"
import { useNavigation } from "@react-navigation/native"
import getFloors from "../lib/floor/getFloors"

const FloorContext: any = createContext(null)

const FloorProvider = ({ children }: any) => {
  const navigation = useNavigation()

  const states: any = navigation.getState()

  const [floors, setFloors] = useState([])
  const [loading, setLoading] = useState(false)
  const [floorName, setFloorName] = useState("")
  const [isEditMode, setIsEditMode] = useState(false)
  const [editFloorId, setEditFloorId] = useState<number | null>(null)

  const buildingId = useMemo(() => {
    return states.routes.find((route: any) => route.name === "Floor").params.buildingId
  }, [states])

  const areaId = useMemo(() => {
    return states.routes.find((route: any) => route.name === "Floor").params.areaId
  }, [states])

  const addNewFloor = async () => {
    const response = await createFloor(floorName)
    setFloorName("")
    if (response?.error) return
    getFloorList()
  }

  const updateFloorById = async () => {
    if (!editFloorId) return
    const response = await updateFloor({
      id: editFloorId,
      name: floorName,
    })

    setIsEditMode(false)
    setFloorName("")

    if (response?.error) return
    getFloorList()
  }

  const deleteFloorById = async (id: number | null) => {
    const response = await deleteFloor(id)
    if (response?.error) return
    getFloorList()
  }

  const openEditMode = (data: IFloor) => {
    setIsEditMode(true)
    setFloorName(data.name)
    setEditFloorId(data.id)
  }

  const closeEditMode = () => {
    setIsEditMode(false)
    setFloorName("")
    setEditFloorId(null)
  }

  const getFloorList = useCallback(async () => {
    setLoading(true)
    const response = await getFloors(areaId)
    setFloors(response)
    setLoading(false)
  }, [areaId])

  useEffect(() => {
    getFloorList()
  }, [getFloorList])

  const value = useMemo(
    () => ({
      floors,
      getFloorList,
      floorName,
      setFloorName,
      addNewFloor,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateFloorById,
      deleteFloorById,
      closeEditMode,
      areaId,
      buildingId
    }),
    [
      floors,
      getFloorList,
      floorName,
      setFloorName,
      addNewFloor,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateFloorById,
      deleteFloorById,
      closeEditMode,
      areaId,
      buildingId
    ],
  )

  return <FloorContext.Provider value={value}>{children}</FloorContext.Provider>
}

export const useFloor = () => {
  const context: any = useContext(FloorContext)
  if (!context) {
    throw new Error("useFloor must be used within a FloorProvider")
  }
  return context
}

export default FloorProvider
