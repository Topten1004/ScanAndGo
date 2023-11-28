from flask_restful import Api
import resources.floor as FloorResource

def floorRouteIndex(app):
    api = Api(app)
    
    api.add_resource(FloorResource.CreateFloor, '/api/floor/create')
    api.add_resource(FloorResource.ReadFloor, '/api/floor/read')
    api.add_resource(FloorResource.ReadAllFloor, '/api/floor/readall')
    api.add_resource(FloorResource.UpdateFloor, '/api/floor/update')
    api.add_resource(FloorResource.DeleteFloor, '/api/floor/delete')