from flask import request
from models.user import UserModel
from models.log import LogModel
from flask_restful import Resource, reqparse
from datetime import timedelta
from flask_jwt_extended import (create_access_token, create_refresh_token, jwt_required, get_jwt_identity)
import datetime

parser = reqparse.RequestParser()
parser.add_argument('username')
parser.add_argument('password')
parser.add_argument('role')

class UserRegister(Resource):
    def post(self):
        print(5)
        data = parser.parse_args()

        if (UserModel.find_by_username(data['username'])):
            return {'message': 'User {} already exists'.format(data['username'])}
        new_user = UserModel(
            username = data['username'],
            password = UserModel.generate_hash(data['password'])
        )
        try:
            new_user.save_to_db()
            return {
                'message': 'User {} was created'.format(data['username'])
            }
        except:
            return {'message': 'Something went wrong'}, 500

class UserLogin(Resource):
    def post(self):
        data = parser.parse_args()
        current_user = UserModel.find_by_username(data['username'])
        print("----->", data['username'], data['password'])
        if not current_user:
            return {'message': 'User {} doesn\'t exists.'.format(data['username']), 'status': -1}
        if UserModel.verify_password(data['password'], current_user.password):
            access_token = create_access_token(identity=data['username'], fresh=False, expires_delta=timedelta(days=1))
            refresh_token = create_refresh_token(identity=data['username'])
            
            user_json = {
                'id': current_user.id,
                'username': current_user.username,
                'role': current_user.role
            }

            new_log = LogModel(
                user_id = current_user.id,
                user_name = current_user.username,
                login_date = datetime.datetime.now().date()
            )
            
            new_log.save_to_db()

            return {
                'message': 'Logged in as {}'.format(current_user.username),
                'access_token': f'{access_token}',
                'refresh_token': refresh_token,
                'status': 1,
                'user': user_json
            }
        
        else:
            return {'message': 'Wrong Password', 'status': 0}

class AllUsers(Resource):
    def get(self):
        return UserModel.return_all()
    @jwt_required()
    def post(self):
        return UserModel.delete_all()
    
class UpdateUser(Resource):
    def put(self):
        data = parser.parse_args()
        return UserModel.update_one(request.args.get('id'), data['role'])
    
class DeleteUser(Resource):
    def delete(self):
        return UserModel.delete_one(request.args.get('id'))