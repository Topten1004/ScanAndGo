from models.role import RoleModel
from flask_restful import Resource

class ReadRole(Resource):
    def get(self):
        return RoleModel.return_all()
