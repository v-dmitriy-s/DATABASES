import { API } from "@/lib/api";
import { Order } from "../types/order";

export class OrderService {
    static async getOrders(): Promise<Order[]> {
        const response = await fetch(`${API.order}/api/orders`, {
            headers: {
                "X-User-Email": "user@example.com" // TODO: Replace with actual user session
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch orders: ${response.statusText}`);
        }

        return response.json();
    }
} 