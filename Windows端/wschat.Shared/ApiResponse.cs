using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace wschat.Shared.Contact
{
    public class ApiResponse
    { 
        public string Code { get; set; }
        public string Message { get; set; }

        public bool Status { get; set; }

        public object Result { get; set; }
        public object Data { get; set; }
    }

    public class ApiResponse<T>
    {
        public string Code { get; set; }
        public string Message { get; set; }

        public bool Status { get; set; }

        public T Result { get; set; }
        public T Data { get; set; }
    }
}
