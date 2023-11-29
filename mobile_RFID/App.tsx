import React from 'react';
import RootNavigator from './app/navigation';
import {Provider} from 'react-redux';
import {store} from './app/store';
import {QueryClient, QueryClientProvider} from 'react-query';

const App = () => {
  const queryClient = React.useRef(
    new QueryClient({
      defaultOptions: {
        queries: {
          refetchOnMount: false,
          refetchOnReconnect: false,
          refetchOnWindowFocus: false,
        },
      },
    }),
  );

  return (
    <Provider store={store}>
      <QueryClientProvider client={queryClient.current}>
        <RootNavigator />
      </QueryClientProvider>
    </Provider>
  );
};

export default App;
