from flask_restful import Api
import resources.role as RoleResource

def roleRouteIndex(app):
    api = Api(app)
    
    api.add_resource(RoleResource.ReadRole, '/api/role/read')