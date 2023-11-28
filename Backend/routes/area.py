from flask_restful import Api
import resources.area as AreaResource

def areaRouteIndex(app):
    api = Api(app)
    
    api.add_resource(AreaResource.CreateArea, '/api/area/create')
    api.add_resource(AreaResource.ReadArea, '/api/area/read')
    api.add_resource(AreaResource.UpdateArea, '/api/area/update')
    api.add_resource(AreaResource.DeleteArea, '/api/area/delete')
    api.add_resource(AreaResource.ReadAllArea, '/api/area/readall')