import * as React from 'react';
import 'styles/global.css';

import Head from 'next/head';

import ProgressBar from 'nextjs-progressbar';
import 'react-toastify/dist/ReactToastify.css'
import { ToastContainer } from 'react-toastify';

import { Provider } from 'react-redux';
import { QueryClient, QueryClientProvider } from 'react-query';
import { store, persistor } from 'store';
import { ReactQueryDevtools } from 'react-query/devtools';
import { LanguageProvider } from 'shared/context/language';
const { PersistGate } = require('redux-persist/integration/react');

import cookie from 'cookie';
import type { IncomingMessage } from 'http';
import { AppContext } from 'next/app';

const keycloakCfg = {
	realm: '',
	url: '',
	clientId: ''
}

interface InitialProps {
	cookies: unknown
}

const App = ({ Component, pageProps, cookies }: any) => {
	const { metaTags } = pageProps;

	const queryClient = React.useRef(
		new QueryClient({
			defaultOptions: {
				queries: {
					refetchOnMount: false,
					refetchOnReconnect: false,
					refetchOnWindowFocus: false
				}
			}
		})
	)

	return (
		<React.Fragment>
			<Head>
				<meta
					name='viewport'
					content='width=device-width, initial-scale=1'
				/>
				{metaTags &&
					Object.entries(metaTags).map(entry => {
						if (!entry[1]) return null
						return (
							<meta
								key={entry[1] as React.Key}
								property={entry[0]}
								content={entry[1] as string}
							/>
						)
					})}
			</Head>
			<Provider store={store}>
				<PersistGate loading={null} persistor={persistor}>
					<QueryClientProvider client={queryClient.current}>
						<LanguageProvider>
							<ProgressBar
								options={{
									showSpinner: false
								}}
							/>
							<SSRKeycloakProvider
								keycloakConfig={keycloakCfg}
								persistor={SSRCookies(cookies)}
							>
								<Component {...pageProps} />
							</SSRKeycloakProvider>
							<ToastContainer />
							{/* <ReactQueryDevtools initialIsOpen={false} /> */}
						</LanguageProvider>
					</QueryClientProvider>
				</PersistGate>
			</Provider>
		</React.Fragment>
	)
}

function parseCookies(req?: IncomingMessage) {
  if (!req || !req.headers) {
    return {}
  }
  return cookie.parse(req.headers.cookie || '')
}

App.getInitialProps = async (context: AppContext) => {
  // Extract cookies from AppContext
  return {
    cookies: parseCookies(context?.ctx?.req)
  }
}

export default App;