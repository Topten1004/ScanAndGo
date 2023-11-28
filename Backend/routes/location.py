from flask_restful import Api
import resources.location as LocationResource

def locationRouteIndex(app):
    api = Api(app)
    
    api.add_resource(LocationResource.CreateLocation, '/api/location/create')
    api.add_resource(LocationResource.ReadLocation, '/api/location/read')
    api.add_resource(LocationResource.UpdateLocation, '/api/location/update')
    api.add_resource(LocationResource.DeleteLocation, '/api/location/delete')