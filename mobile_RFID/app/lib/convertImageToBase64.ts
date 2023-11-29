const convertImageToBase64 = async (img: any, handleLoaded: any) => {
  if (!img?.uri) return

  try {
    const response = await fetch(img?.uri)
    const blob = await response.blob()

    const reader = new FileReader()
    reader.onloadend = () => {
      if (typeof reader.result == "string") {
        const base64Image = reader.result.split(",")[1]

        handleLoaded(`data:image/png;base64,${base64Image}`)
      }
    }

    reader.readAsDataURL(blob)
  } catch (error) {
    return { error }
  }
}

export default convertImageToBase64
