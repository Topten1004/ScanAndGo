from flask_restful import Api
import resources.sublocation as SubLocationResource

def sublocationRouteIndex(app):
    api = Api(app)
    
    api.add_resource(SubLocationResource.CreateSubLocation, '/api/sublocation/create')
    api.add_resource(SubLocationResource.ReadSubLocation, '/api/sublocation/read')
    api.add_resource(SubLocationResource.UpdateSubLocation, '/api/sublocation/update')
    api.add_resource(SubLocationResource.DeleteSubLocation, '/api/sublocation/delete')
    api.add_resource(SubLocationResource.ReadAllLocation, '/api/sublocation/readall')