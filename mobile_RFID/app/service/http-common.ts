import axios, { AxiosRequestConfig } from "axios"

const API_URL = "https://api-villedenoumea.scanandgo.nc/api"

const setHeaders = async () => {
  // const session = store.getState().app.session

  const headers: {
    "Content-Type": string
    Authorization?: string
    SessionId?: string
  } = {
    "Content-Type": "application/json",
  }
  // if (session?.sessionId) {
  //     headers['SessionId'] = `${session?.sessionId}`
  // } else if (store.getState().auth?.account) {
  //     sessionExpired();
  // }
  // if (store.getState().auth?.token) {
  //     headers.Authorization = `Bearer ${store.getState().auth?.token}`
  // }
  axios.defaults.headers.common = headers
}

// const sessionExpired = () => {
//     store.dispatch(setSessionExpired())
// }

const http = {
  async get(endpoint: string, config?: AxiosRequestConfig<any>) {
    // await setHeaders();

    try {
      const res = axios.get(`${API_URL}${endpoint}`, config)
      return res
    } catch (e: any) {
      if (e && e.response && e.response.data && e.response.data.status === "invalid_token") {
      } else {
        err: e
      }
    }
  },
  async post(endpoint: string, params: any, config?: AxiosRequestConfig<any>) {
    await setHeaders()

    try {
      const res = await axios.post(`${API_URL}${endpoint}`, params, config)
      return res
    } catch (e: any) {
      if (e && e.response && e.response.data && e.response.data.stats === "invalid_token") {
      } else {
        err: e
      }
    }
  },
  async put(endpoint: string, params: any, config?: AxiosRequestConfig<any>) {
    // await setHeaders()

    try {
      const res = await axios.put(`${API_URL}${endpoint}`, params, config)
      return res
    } catch (e: any) {
      if (e && e.response && e.response.data && e.response.data.status === "invalid_token") {
        // sessionExpired()
      } else return e
    }
  },
  async patch(endpoint: string, params: any, config?: AxiosRequestConfig<any>) {
    // await setHeaders()

    try {
      const res = await axios.patch(`${API_URL}${endpoint}`, params, config)
      return res
    } catch (e: any) {
      if (e && e.response && e.response.data && e.response.data.status === "invalid_token") {
        // sessionExpired()
      } else return e
    }
  },
  async delete(endpoint: string, config?: AxiosRequestConfig<any>) {
    // await setHeaders()

    try {
      const res = await axios.delete(`${API_URL}${endpoint}`, config)
      return res
    } catch (e: any) {
      if (e && e.response && e.response.data && e.response.data.status === "invalid_token") {
        // sessionExpired()
      } else return e
    }
  },
}

export default http
