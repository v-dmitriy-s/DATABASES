export interface OrderItem {
    name: string;
    quantity: number;
    price: number;
    image: string;
}

export interface Order {
    id: string;
    createdAt: string;
    total: number;
    status: string;
    paymentMethod: string;
    trackingNumber?: string;
    shipping: number;
    items: OrderItem[];
}

export type OrderStatus = 'Delivered' | 'Shipped' | 'Processing' | 'Pending'; 