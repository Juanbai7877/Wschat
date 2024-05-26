using Prism.Events;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace wschat.Common.Events
{
    public class CodeModel
    {
        public string Filter { get; set; }
        public string Message { get; set; }
    }
    public class CodeEvent: PubSubEvent<CodeModel>
    {
    }
}
