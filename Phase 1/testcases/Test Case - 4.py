#!/usr/bin/env python
# coding: utf-8

# In[2]:


import requests

#the base URLs for the services
userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

showId=1
seatsBooked=2

def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def get_wallet(user_id):
    response = requests.get(walletServiceURL + f"/wallets/{user_id}")
    return response

def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action":action, "amount":amount})
    return response

def get_show_details(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    return response   
    

def get_booking_details(user_id):
    response= requests.get(bookingServiceURL+f"/{user_id}")
    return response

def delete_show(user_id,show_id):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{user_id}/shows/{show_id}")
    return response

def delete_All_bookings_of_user(user_id):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{user_id}")
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")  

def delete_particular_user(user_id):
    response = requests.delete(userServiceURL+f"/users/{user_id}")
    return response


def test_user_booking_cancellation_flow():
    try:
        # Step 0: Delete All Users
        delete_users()
        # Step 1: Create a new user
        user_response=create_user("Rajesh", "rajjo@example.com")
        assert user_response.status_code == 201
        user_id = user_response.json()["id"]

        # Step 2: Get wallet details for the new user (should be initialized with zero balance)
        wallet_response = update_wallet(user_id , "credit", 2000)
        assert wallet_response.status_code == 200
        assert wallet_response.json()["user_id"] == user_id
        assert wallet_response.json()["balance"] == 2000

        # Step 3: Create a booking for the user
        booking_payload = {"show_id": 1, "user_id": user_id, "seats_booked": 2}
        booking_response=requests.post(bookingServiceURL + "/bookings", json=booking_payload)
        assert booking_response.status_code == 200
        booking_id = booking_response.json()["id"]

        # Step 4: Get wallet details after the booking (balance should be deducted)
        getShow=get_show_details(showId)
        wallet_response_after_booking =get_wallet(user_id)
        assert wallet_response_after_booking.status_code == 200
        assert wallet_response_after_booking.json()["user_id"] == user_id
        assert wallet_response_after_booking.json()["balance"] == wallet_response.json()["balance"] - getShow.json()["price"] * seatsBooked  # Assuming deduction occurred

        # Step 5: Cancel the booking
        cancel_ALL_booking_Of_user_response = delete_All_bookings_of_user(user_id)
        assert cancel_ALL_booking_Of_user_response.status_code == 200

        # Step 6: Get wallet details after booking cancellation (balance should be returned)
        wallet_response_after_cancellation = get_wallet(user_id)
        assert wallet_response_after_cancellation.status_code == 200
        assert wallet_response_after_cancellation.json()["user_id"] == user_id
        assert wallet_response_after_cancellation.json()["balance"] ==  wallet_response.json()["balance"]  # Assuming balance is returned

        # Step 7: Get booking details (should not exist after cancellation)
        get_booking_response = get_booking_details(user_id)
        assert get_booking_response.status_code == 404

        # Step 8: Delete the user and associated data
        delete_user_response = delete_particular_user(user_id)
        assert delete_user_response.status_code == 200

        wallet_of_deleted_user = get_wallet(user_id)
        assert wallet_of_deleted_user.status_code == 404
        print("Test Passed")

    except:
        print("Tests Failed or Some Exception Occurred")

# Run the test
test_user_booking_cancellation_flow()


# In[ ]:




