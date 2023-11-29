import { createContext, useContext, useMemo, useState, useEffect, useCallback } from "react"
import createLocation from "../lib/location/createLocation"
import { ILocation } from "../types/location"
import deleteLocation from "../lib/location/deleteLocation"
import updateLocation from "../lib/location/updateLocation"
import { useNavigation } from "@react-navigation/native"
import getLocations from "../lib/location/getLocations"
import { PermissionsAndroid } from "react-native"
import showToast from "../shared/ui/showToast"
import capture from "../lib/capture"
import { STEPS } from "../consts/detailStep"
import createInventory from "../lib/inventory/createInventory"
import { IItem } from "../types/item"
import { ICategory } from "../types/category"

const DetailLocationContext: any = createContext(null)

const DetailLocationProvider = ({ children }: any) => {
  const navigation: any = useNavigation()
  const states: any = navigation.getState()
  const [locations, setLocations] = useState<any>([])
  const [loading, setLoading] = useState(false)
  const [locationName, setLocationName] = useState("")
  const [isEditMode, setIsEditMode] = useState(false)
  const [editLocationId, setEditLocationId] = useState<number | null>(null)
  const [capturedImg, setCapturedImg] = useState<any>(null)
  const [barCode, setBarCode] = useState("")
  const [currentStep, setCurrentStep] = useState(STEPS.SELECT_CATEGORY_ITEM)
  const [comment, setComment] = useState("")

  const currentLocationId = useMemo(() => {
    if (locations.length) return locations[0]?.id
  }, [locations])

  const floorId = useMemo(() => {
    return states.routes.find((route: any) => route.name === "DetailLocation").params.floorId
  }, [states])

  const buildingId = useMemo(() => {
    return states.routes.find((route: any) => route.name === "DetailLocation").params.buildingId
  }, [states])

  const areaId = useMemo(() => {
    return states.routes.find((route: any) => route.name === "DetailLocation").params.areaId
  }, [states])

  const evaluate = async (level: string, item: IItem, selectedCategory: ICategory) => {
    if (!barCode) {
      showToast("Bar Code should not be an empty.")
      return
    }

    if (!comment) {
      showToast("Comment should not be an empty.")
      return
    }


    const response = await createInventory(
      barCode,
      level,
      capturedImg,
      comment,
      selectedCategory.id,
      item.id,
      buildingId,
      areaId,
      floorId,
      locations[0].id
    )
    if (!response?.error) showToast("Thank you!")
    setCurrentStep(STEPS.SELECT_CATEGORY_ITEM)
  }

  const requestCameraPermission = useCallback(async () => {
    try {
      const granted = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA, {
        title: "App Camera Permission",
        message: "App needs access to your camera",
        buttonNeutral: "Ask Me Later",
        buttonNegative: "Cancel",
        buttonPositive: "OK",
      })
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        showToast("Camera permission given")
      } else {
        showToast("Camera permission denied")
      }
    } catch (err) {
      console.warn(err)
    }
  }, [])

  const addNewLocation = async () => {
    const response = await createLocation(locationName)
    setLocationName("")
    if (response?.error) return
    getLocationList()
  }

  const updateLocationById = async () => {
    if (!editLocationId) return
    const response = await updateLocation({
      id: editLocationId,
      name: locationName,
      floorId,
      imgData: capturedImg,
    })

    setIsEditMode(false)
    setLocationName("")

    if (response?.error) return
    getLocationList()
  }

  const deleteLocationById = async (id: number | null) => {
    const response = await deleteLocation(id)
    if (response?.error) return
    getLocationList()
  }

  const openEditMode = (data: ILocation) => {
    setIsEditMode(true)
    setLocationName(data.name)
    setEditLocationId(data.id)
  }

  const closeEditMode = () => {
    setIsEditMode(false)
    setLocationName("")
    setEditLocationId(null)
  }

  const getLocationList = useCallback(async () => {
    setLoading(true)
    const response = await getLocations(floorId)
    setLocations(response)
    setLoading(false)
  }, [floorId])

  useEffect(() => {
    getLocationList()
  }, [getLocationList])

  useEffect(() => {
    requestCameraPermission()
  }, [requestCameraPermission])

  useEffect(() => {
    if (locations.length && !capturedImg) {
      const detailLocation: ILocation = locations[0]
      if (!detailLocation.imgData) capture(setCapturedImg)
    }
  }, [locations, capturedImg])

  const value = useMemo(
    () => ({
      locations,
      getLocationList,
      locationName,
      setLocationName,
      addNewLocation,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateLocationById,
      deleteLocationById,
      closeEditMode,
      capturedImg,
      barCode,
      setBarCode,
      currentStep,
      setCurrentStep,
      evaluate,
      currentLocationId,
      buildingId,
      areaId,
      comment,
      setComment
    }),
    [
      locations,
      getLocationList,
      locationName,
      setLocationName,
      addNewLocation,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateLocationById,
      deleteLocationById,
      closeEditMode,
      capturedImg,
      barCode,
      setBarCode,
      currentStep,
      setCurrentStep,
      evaluate,
      currentLocationId,
      buildingId,
      areaId,
      comment,
      setComment
    ],
  )

  return <DetailLocationContext.Provider value={value}>{children}</DetailLocationContext.Provider>
}

export const useDetailLocation = () => {
  const context: any = useContext(DetailLocationContext)
  if (!context) {
    throw new Error("useDetailLocation must be used within a DetailLocationProvider")
  }
  return context
}

export default DetailLocationProvider
